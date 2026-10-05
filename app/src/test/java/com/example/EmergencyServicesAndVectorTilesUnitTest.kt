package com.example

import android.content.Context
import com.example.data.emergency.EmergencyCategory
import com.example.data.emergency.EmergencyServicesRepository
import com.example.data.maps.BalasoreOfflineTileProvider
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.mockito.Mockito.mock

/**
 * Unit Test Suite for Offline Vector Tile Provider and
 * Nearest Emergency Services Distance/Sorting Engine.
 */
class EmergencyServicesAndVectorTilesUnitTest {

    @Test
    fun testHaversineDistanceCalculation() {
        // Balasore Junction coordinates
        val userLat = 21.4934
        val userLng = 86.9135

        // DHH Balasore (Phandi Chhak: 21.4910, 86.9280)
        val dhh = EmergencyServicesRepository.allServices.first { it.id == "hosp_dhh" }
        val distanceDhh = dhh.distanceFrom(userLat, userLng)

        // DHH is ~1.5 km east of Railway Station
        assertTrue("Distance to DHH should be between 1.0 km and 2.5 km", distanceDhh in 1.0..2.5)

        val formatted = dhh.formattedDistance(userLat, userLng)
        assertTrue("Formatted distance should contain 'km'", formatted.contains("km"))
    }

    @Test
    fun testCompassBearingCalculation() {
        val userLat = 21.4934
        val userLng = 86.9135

        // Chandipur is East / South-East of Balasore Junction
        val marinePolice = EmergencyServicesRepository.allServices.first { it.id == "pol_marine_chandipur" }
        val bearing = marinePolice.compassBearing(userLat, userLng)

        assertTrue("Chandipur should be in East or South-East direction", bearing.contains("East"))
    }

    @Test
    fun testNearestEmergencyServicesSorting() {
        // User at Balasore Junction
        val userLat = 21.4934
        val userLng = 86.9135

        val sortedServices = EmergencyServicesRepository.getNearestServices(userLat, userLng)
        assertTrue("Must return emergency services", sortedServices.isNotEmpty())

        // Verify strictly ascending distance ordering
        for (i in 0 until sortedServices.size - 1) {
            val d1 = sortedServices[i].distanceFrom(userLat, userLng)
            val d2 = sortedServices[i + 1].distanceFrom(userLat, userLng)
            assertTrue("Services must be sorted from nearest to farthest ($d1 <= $d2)", d1 <= d2)
        }
    }

    @Test
    fun testCategoryFiltering() {
        val userLat = 21.4934
        val userLng = 86.9135

        // Filter: Hospitals Only
        val hospitals = EmergencyServicesRepository.getNearestServices(userLat, userLng, EmergencyCategory.HOSPITAL)
        assertTrue(hospitals.isNotEmpty())
        assertTrue(hospitals.all { it.category == EmergencyCategory.HOSPITAL })

        // Filter: Police Only
        val police = EmergencyServicesRepository.getNearestServices(userLat, userLng, EmergencyCategory.POLICE)
        assertTrue(police.isNotEmpty())
        assertTrue(police.all { it.category == EmergencyCategory.POLICE })

        // Filter: Cyclone Shelters Only
        val shelters = EmergencyServicesRepository.getNearestServices(userLat, userLng, EmergencyCategory.CYCLONE_SHELTER)
        assertTrue(shelters.isNotEmpty())
        assertTrue(shelters.all { it.category == EmergencyCategory.CYCLONE_SHELTER })

        // Filter: Fire Rescue Only
        val fire = EmergencyServicesRepository.getNearestServices(userLat, userLng, EmergencyCategory.FIRE_RESCUE)
        assertTrue(fire.isNotEmpty())
        assertTrue(fire.all { it.category == EmergencyCategory.FIRE_RESCUE })
    }

    @Test
    fun testOfflineVectorTileCoordinates() {
        val lat = 21.4934
        val lng = 86.9135
        val zoom = 14

        val tileX = BalasoreOfflineTileProvider.latLngToTileX(lng, zoom)
        val tileY = BalasoreOfflineTileProvider.latLngToTileY(lat, zoom)

        assertTrue("Tile X should be positive", tileX > 0)
        assertTrue("Tile Y should be positive", tileY > 0)
        val maxTileIndex = 1 shl zoom
        assertTrue("Tile X within zoom bounds", tileX < maxTileIndex)
        assertTrue("Tile Y within zoom bounds", tileY < maxTileIndex)
    }

    @Test
    fun testEmergencyContactsAndPreservesIntegrity() {
        val services = EmergencyServicesRepository.allServices
        assertTrue("Must have at least 10 emergency services registered", services.size >= 10)

        services.forEach { service ->
            assertTrue("Name must not be blank", service.name.isNotBlank())
            assertTrue("Odia name must not be blank", service.odiaName.isNotBlank())
            assertTrue("Phone must not be blank", service.phone.isNotBlank())
            assertTrue("Latitude must be in Balasore district (~21.0 to 22.0)", service.latitude in 21.0..22.0)
            assertTrue("Longitude must be in Balasore district (~86.5 to 87.5)", service.longitude in 86.5..87.5)
        }
    }
}
