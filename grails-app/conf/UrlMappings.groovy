class UrlMappings {

	static mappings = {
    "/api/wines/search/$query" (controller:"wine", parseRequest: true) {
      action = [GET:"search"]
			constraints {
				// apply constraints here
			}
		}
    "/api/wines/$id?" (resource:"wine") {
			constraints {
				// apply constraints here
			}
		}

		"/"(view:"/index")
		"500"(view:'/error')
	}
}
