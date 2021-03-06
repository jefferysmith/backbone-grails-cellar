package org.coenraets.cellar2

import grails.test.mixin.*

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(WineService)
@Mock(Wine)
class WineServiceTests {

    @Test
    void testFindAll() {
        new Wine().save(validate: false)
        new Wine().save(validate: false)
        new Wine().save(validate: false)
        def result = service.findAll()
        assertEquals(3, result.size())
    }

    @Test
    void testFindByName_single() {
        new Wine(name: 'wineName1').save(validate: false)
        new Wine(name: 'wineName2').save(validate: false)
        def results = service.findByName('wineName1')
        assertEquals(1, results.size())
        assertEquals('wineName1', results[0].name)
    }

    @Test
    void testFindByName_multiple() {
        new Wine(name: 'wineName1').save(validate: false)
        new Wine(name: 'wineName2').save(validate: false)
        def results = service.findByName('winename')
        assertEquals(2, results.size())
    }

        @Test
    void testFindByName_notFound() {
        def results = service.findByName('none')
        assertTrue(results.isEmpty())
    }

    @Test
    void testFindById() {
        new Wine(id: 1, name: 'wineName1').save(validate: false)
        new Wine(id: 2, name: 'wineName2').save(validate: false)
        def foundWine = service.findById(2)
        assertNotNull(foundWine)
    }

    @Test
    void testCreate() {
        def newWine = new Wine( name: 'name', grapes: 'grapes',
                country: 'country', region: 'region', year: 'year',
                picture: 'picture',
                description: 'description' )
        def createdWine = service.create(newWine)
        assertNotNull(createdWine)
        assertNotNull(createdWine.id)
    }

    @Test
    void testCreate_existing() {
        def newWine = new Wine( name: 'name', grapes: 'grapes',
                country: 'country', region: 'region', year: 'year',
                picture: 'picture',
                description: 'description' )
        def createdWine = service.create(newWine)
        assertNotNull(createdWine)
        createdWine.name = "newName"
        shouldFail(IllegalArgumentException) { service.create(createdWine) }
    }

    @Test
    void testUpdate() {
        def newWine = new Wine( name: 'name', grapes: 'grapes',
                country: 'country', region: 'region', year: 'year',
                picture: 'picture',
                description: 'description' )
        def createdWine = service.create(newWine)
        assertNotNull(createdWine)
        createdWine.name = "newName"
        def updatedWine = service.update(createdWine)
        assertNotNull(updatedWine)
        assertEquals("newName", updatedWine.name)
    }

    @Test
    void testUpdate_new() {
        def newWine = new Wine( name: 'name', grapes: 'grapes',
                country: 'country', region: 'region', year: 'year',
                picture: 'picture',
                description: 'description' )
        shouldFail(IllegalArgumentException) { service.update(newWine) }
    }

    @Test
    void testDelete() {
        new Wine(id: 1, name: 'wineName1').save(validate: false)
        new Wine(id: 2, name: 'wineName2').save(validate: false)
        service.remove(2)
        assertFalse(Wine.exists(2))
    }

    @Test
    void testDelete_notFound() {
        shouldFail(IllegalArgumentException) { service.remove(2) }
    }
}
