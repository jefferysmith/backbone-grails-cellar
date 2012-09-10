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
        assertEquals 3, result.size()
    }
}
