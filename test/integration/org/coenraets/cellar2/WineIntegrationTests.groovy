package org.coenraets.cellar2

import static org.junit.Assert.*

import org.junit.*

class WineIntegrationTests {

    @Before
    void setUp() {
        // Setup logic here
    }

    @After
    void tearDown() {
        // Tear down logic here
    }

    @Test
    void testSaveWine() {
        def wine = createWine()
        assertNotNull wine.save()
        assertNotNull wine.id

        def foundWine = Wine.get(wine.id)
        assertEquals 'CHATEAU DE SAINT COSME', foundWine.name
    }

    @Test
    void testSaveAndEditWine() {
        def wine = createWine()
        assertNotNull wine.save()
        assertNotNull wine.id

        def foundWine = Wine.get(wine.id)
        foundWine.year = '2008'
        foundWine.save()

        def editedWine = Wine.get(wine.id)
        assertEquals '2008', editedWine.year
    }

        /**
     * Utility to create a new wine instance
     */
    private Wine createWine() {
        def wine = new Wine( name: 'CHATEAU DE SAINT COSME', grapes: 'Grenache / Syrah',
            country: 'France', region: 'Southern Rhone / Gigondas', year: '2009',
            picture: 'saint_cosme.jpg',
            description: 'The aromas of fruit and spice give one a hint of the light drinkability of this lovely wine, which makes an excellent complement to fish dishes.' )
    }
}
