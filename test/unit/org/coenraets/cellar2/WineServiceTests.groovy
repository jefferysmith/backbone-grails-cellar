package org.coenraets.cellar2

import grails.test.mixin.*

import org.junit.*

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(WineService)
@Mock(Wine)
class WineServiceTests {

    void testFindAll() {
        new Wine().save(validate: false)
        new Wine().save(validate: false)
        new Wine().save(validate: false)
        def result = service.findAll()
        assertEquals(3, result.size())
    }

    void testFindByName() {
        new Wine(name: 'wineName1').save(validate: false)
        new Wine(name: 'wineName2').save(validate: false)
        def foundWine = service.findByName('wineName1')
        assertEquals('wineName1', foundWine.name)
    }

    void testFindByName_notFound() {
        def foundWine = service.findByName('none')
        assertNull(foundWine)
    }

    void testFindById() {
        new Wine(id: 1, name: 'wineName1').save(validate: false)
        new Wine(id: 2, name: 'wineName2').save(validate: false)
        def foundWine = service.findById(2)
        assertNotNull(foundWine)
    }
}
