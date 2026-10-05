package com.example.ui.features.maps

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material.icons.filled.ZoomOut
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.maps.NavPointCategory
import com.example.data.maps.OfflineNavPoint
import com.example.data.maps.OfflineNavRoute
import com.example.data.model.AppLanguage
import com.example.ui.components.geoToCanvasOffset
import com.google.android.gms.maps.model.LatLng
import kotlin.math.hypot

/**
 * 100% Offline Vector Tile Canvas Provider (inspired by Mapsforge & Mapbox vector tiles).
 * Renders Balasore essential roads, river basins, coastline, emergency facilities,
 * and key landmarks completely on-device without network or Google Play Services.
 */
@Composable
fun BalasoreOfflineVectorTileCanvas(
    userLocation: LatLng,
    landmarks: List<OfflineNavPoint>,
    selectedPoi: OfflineNavPoint?,
    onSelectPoi: (OfflineNavPoint) -> Unit,
    activeRoute: OfflineNavRoute?,
    isOutageMode: Boolean = false,
    language: AppLanguage = AppLanguage.ENGLISH,
    modifier: Modifier = Modifier
) {
    // Geographic Bounding Box for Balasore District
    val minLat = 21.30
    val maxLat = 21.68
    val minLng = 86.68
    val maxLng = 87.22

    var zoomScale by remember { mutableFloatStateOf(1.0f) }
    var panOffset by remember { mutableStateOf(Offset.Zero) }

    val transformableState = rememberTransformableState { zoomChange, offsetChange, _ ->
        zoomScale = (zoomScale * zoomChange).coerceIn(0.75f, 4.5f)
        panOffset += offsetChange
    }

    // Beacon pulsing animation
    val infiniteTransition = rememberInfiniteTransition(label = "vector_beacon")
    val pulseRadius by infiniteTransition.animateFloat(
        initialValue = 6f,
        targetValue = 24f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_radius"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 0.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1300, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulse_alpha"
    )

    // Base background colors
    val bgColor = if (isOutageMode) Color(0xFF0A0F1D) else Color(0xFF0F172A)
    val landColor = if (isOutageMode) Color(0xFF131D31) else Color(0xFF1E293B)
    val waterColor = if (isOutageMode) Color(0xFF032644) else Color(0xFF0284C7).copy(alpha = 0.65f)
    val nh16Color = Color(0xFFF59E0B)
    val sh19Color = Color(0xFF38BDF8)
    val localRoadColor = Color(0xFF64748B)

    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(14.dp))
            .background(bgColor)
            .transformable(state = transformableState)
            .testTag("offline_vector_tile_canvas_container")
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .testTag("offline_vector_tile_canvas")
                .pointerInput(landmarks, zoomScale, panOffset) {
                    detectTapGestures { tapOffset ->
                        val cWidth = size.width.toFloat()
                        val cHeight = size.height.toFloat()
                        if (cWidth <= 0 || cHeight <= 0) return@detectTapGestures

                        // Tap hit test against landmarks
                        for (poi in landmarks) {
                            val pt = geoToCanvasOffset(
                                poi.location.latitude, poi.location.longitude,
                                minLat, maxLat, minLng, maxLng,
                                cWidth, cHeight, zoomScale, panOffset
                            )
                            if (hypot(tapOffset.x - pt.x, tapOffset.y - pt.y) < 32f) {
                                onSelectPoi(poi)
                                break
                            }
                        }
                    }
                }
        ) {
            val cWidth = size.width
            val cHeight = size.height

            // 1. Draw Simulated Vector Tile Grid Lines & Tile Boundary IDs (Mapsforge Style)
            val tileCols = 6
            val tileRows = 5
            val colStep = cWidth * zoomScale / tileCols
            val rowStep = cHeight * zoomScale / tileRows

            for (c in -1..tileCols + 1) {
                val x = c * colStep + panOffset.x
                drawLine(
                    color = Color(0xFF334155).copy(alpha = 0.25f),
                    start = Offset(x, 0f),
                    end = Offset(x, cHeight),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }
            for (r in -1..tileRows + 1) {
                val y = r * rowStep + panOffset.y
                drawLine(
                    color = Color(0xFF334155).copy(alpha = 0.25f),
                    start = Offset(0f, y),
                    end = Offset(cWidth, y),
                    strokeWidth = 1f,
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
                )
            }

            // 2. Draw Bay of Bengal Coastline & Sea (East sector: Lng 87.00 to 87.22)
            val coastPath = Path()
            val cTop = geoToCanvasOffset(21.68, 87.16, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cDigha = geoToCanvasOffset(21.60, 87.20, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cTalasari = geoToCanvasOffset(21.57, 87.18, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cKasafal = geoToCanvasOffset(21.51, 87.10, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cBalaramgadi = geoToCanvasOffset(21.48, 87.05, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cChandipur = geoToCanvasOffset(21.46, 87.02, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val cSouth = geoToCanvasOffset(21.30, 86.98, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)

            coastPath.moveTo(cTop.x, cTop.y)
            coastPath.lineTo(cDigha.x, cDigha.y)
            coastPath.lineTo(cTalasari.x, cTalasari.y)
            coastPath.lineTo(cKasafal.x, cKasafal.y)
            coastPath.lineTo(cBalaramgadi.x, cBalaramgadi.y)
            coastPath.lineTo(cChandipur.x, cChandipur.y)
            coastPath.lineTo(cSouth.x, cSouth.y)
            coastPath.lineTo(cWidth * 2, cHeight * 2)
            coastPath.lineTo(cWidth * 2, 0f)
            coastPath.close()

            drawPath(coastPath, color = waterColor)

            // Coastline Accent
            val coastLinePath = Path().apply {
                moveTo(cTop.x, cTop.y)
                lineTo(cDigha.x, cDigha.y)
                lineTo(cTalasari.x, cTalasari.y)
                lineTo(cKasafal.x, cKasafal.y)
                lineTo(cBalaramgadi.x, cBalaramgadi.y)
                lineTo(cChandipur.x, cChandipur.y)
                lineTo(cSouth.x, cSouth.y)
            }
            drawPath(coastLinePath, color = Color(0xFF38BDF8), style = Stroke(width = 2.5f))

            // 3. Budhabalanga River Corridor
            val riverPath = Path()
            val r1 = geoToCanvasOffset(21.58, 86.78, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val r2 = geoToCanvasOffset(21.52, 86.88, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val r3 = geoToCanvasOffset(21.49, 86.93, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val r4 = geoToCanvasOffset(21.47, 87.04, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            riverPath.moveTo(r1.x, r1.y)
            riverPath.cubicTo(r2.x, r2.y, r3.x, r3.y, r4.x, r4.y)
            drawPath(riverPath, color = Color(0xFF0EA5E9), style = Stroke(width = 4f * zoomScale.coerceAtLeast(1f)))

            // 4. Primary Road Corridors (NH-16, SH-19, Coastal Road)
            // NH-16 (North to South trunk corridor through Balasore town)
            val nh16Path = Path()
            val nhNorth = geoToCanvasOffset(21.68, 87.05, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val nhMid = geoToCanvasOffset(21.50, 86.92, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val nhSouth = geoToCanvasOffset(21.30, 86.75, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            nh16Path.moveTo(nhNorth.x, nhNorth.y)
            nh16Path.lineTo(nhMid.x, nhMid.y)
            nh16Path.lineTo(nhSouth.x, nhSouth.y)
            drawPath(nh16Path, color = nh16Color, style = Stroke(width = 5f, cap = StrokeCap.Round))

            // SH-19 (Nilagiri - Balasore connector)
            val sh19Path = Path()
            val shNilagiri = geoToCanvasOffset(21.46, 86.77, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val shTown = geoToCanvasOffset(21.49, 86.92, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            sh19Path.moveTo(shNilagiri.x, shNilagiri.y)
            sh19Path.lineTo(shTown.x, shTown.y)
            drawPath(sh19Path, color = sh19Color, style = Stroke(width = 3.5f, cap = StrokeCap.Round))

            // Chandipur Coastal Road
            val coastalRoadPath = Path()
            val crStart = geoToCanvasOffset(21.49, 86.93, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            val crBeach = geoToCanvasOffset(21.47, 87.01, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
            coastalRoadPath.moveTo(crStart.x, crStart.y)
            coastalRoadPath.lineTo(crBeach.x, crBeach.y)
            drawPath(coastalRoadPath, color = Color(0xFF10B981), style = Stroke(width = 3f, cap = StrokeCap.Round))

            // 5. Active Turn-by-Turn Route Polyline (if calculating route)
            if (activeRoute != null && activeRoute.pathPoints.size >= 2) {
                val routePath = Path()
                val p0 = activeRoute.pathPoints[0]
                val startPt = geoToCanvasOffset(p0.latitude, p0.longitude, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
                routePath.moveTo(startPt.x, startPt.y)

                for (i in 1 until activeRoute.pathPoints.size) {
                    val p = activeRoute.pathPoints[i]
                    val pt = geoToCanvasOffset(p.latitude, p.longitude, minLat, maxLat, minLng, maxLng, cWidth, cHeight, zoomScale, panOffset)
                    routePath.lineTo(pt.x, pt.y)
                }

                // Route halo & main line
                drawPath(
                    routePath,
                    color = Color(0xFFEF4444).copy(alpha = 0.35f),
                    style = Stroke(width = 10f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
                drawPath(
                    routePath,
                    color = Color(0xFFEF4444),
                    style = Stroke(width = 4f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )
            }

            // 6. Draw Essential Landmarks Nodes (Hospitals, Police, Shelters, Heritage, Transit)
            landmarks.forEach { poi ->
                val pt = geoToCanvasOffset(
                    poi.location.latitude, poi.location.longitude,
                    minLat, maxLat, minLng, maxLng,
                    cWidth, cHeight, zoomScale, panOffset
                )

                val isSelected = selectedPoi?.id == poi.id
                val poiColor = when (poi.category) {
                    NavPointCategory.HOSPITAL -> Color(0xFFEF4444)
                    NavPointCategory.POLICE_DEFENSE -> Color(0xFF3B82F6)
                    NavPointCategory.CYCLONE_SHELTER -> Color(0xFFF59E0B)
                    NavPointCategory.FIRE_RESCUE -> Color(0xFFF97316)
                    NavPointCategory.TRANSIT -> Color(0xFF10B981)
                    NavPointCategory.HERITAGE_TOURISM -> Color(0xFFA855F7)
                }

                // Highlight halo for selected node
                if (isSelected) {
                    drawCircle(
                        color = Color.White.copy(alpha = 0.3f),
                        radius = 18f,
                        center = pt
                    )
                    drawCircle(
                        color = Color.White,
                        radius = 12f,
                        center = pt,
                        style = Stroke(width = 2.5f)
                    )
                }

                drawCircle(
                    color = poiColor,
                    radius = if (isSelected) 8f else 6f,
                    center = pt
                )
                drawCircle(
                    color = Color.White,
                    radius = if (isSelected) 4f else 2.5f,
                    center = pt
                )

                // Landmark label (render on-canvas if zoom > 1.2f)
                if (zoomScale >= 1.15f || isSelected) {
                    val paint = android.graphics.Paint().apply {
                        color = if (isSelected) android.graphics.Color.WHITE else android.graphics.Color.LTGRAY
                        textSize = (10f * zoomScale.coerceIn(1f, 1.6f))
                        typeface = android.graphics.Typeface.DEFAULT_BOLD
                        isAntiAlias = true
                        setShadowLayer(3f, 1f, 1f, android.graphics.Color.BLACK)
                    }
                    val label = if (language == AppLanguage.ODIA) poi.nameOr else poi.nameEn
                    drawContext.canvas.nativeCanvas.drawText(
                        label,
                        pt.x + 10f,
                        pt.y + 4f,
                        paint
                    )
                }
            }

            // 7. Simulated User Position with pulsating beacon
            val userPt = geoToCanvasOffset(
                userLocation.latitude, userLocation.longitude,
                minLat, maxLat, minLng, maxLng,
                cWidth, cHeight, zoomScale, panOffset
            )

            // Pulse wave
            drawCircle(
                color = Color(0xFF38BDF8).copy(alpha = pulseAlpha),
                radius = pulseRadius,
                center = userPt
            )
            // Core dot
            drawCircle(
                color = Color(0xFF0284C7),
                radius = 7f,
                center = userPt
            )
            drawCircle(
                color = Color.White,
                radius = 3.5f,
                center = userPt
            )
        }

        // On-screen Zoom & Reset Controls
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                IconButton(
                    onClick = { zoomScale = (zoomScale * 1.25f).coerceAtMost(4.5f) },
                    modifier = Modifier.size(34.dp).testTag("btn_vector_zoom_in")
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomIn,
                        contentDescription = "Zoom In",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                IconButton(
                    onClick = { zoomScale = (zoomScale / 1.25f).coerceAtLeast(0.75f) },
                    modifier = Modifier.size(34.dp).testTag("btn_vector_zoom_out")
                ) {
                    Icon(
                        imageVector = Icons.Default.ZoomOut,
                        contentDescription = "Zoom Out",
                        tint = Color.White,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            Surface(
                shape = CircleShape,
                color = Color(0xFF0F172A).copy(alpha = 0.85f),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF334155))
            ) {
                IconButton(
                    onClick = {
                        zoomScale = 1.0f
                        panOffset = Offset.Zero
                    },
                    modifier = Modifier.size(34.dp).testTag("btn_vector_reset_pan")
                ) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "Reset Pan",
                        tint = Color(0xFF38BDF8),
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}
