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
    void testFindByName() {
        new Wine(name: 'wineName1').save(validate: false)
        new Wine(name: 'wineName2').save(validate: false)
        def foundWine = service.findByName('wineName1')
        assertEquals('wineName1', foundWine.name)
    }

    @Test
    void testFindByName_notFound() {
        def foundWine = service.findByName('none')
        assertNull(foundWine)
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
        shouldFail(IllegalArgumentException) {
            service.create(createdWine)
        }
    }
}
