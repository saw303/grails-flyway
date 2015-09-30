package grails.flyway

import grails.plugins.*
import org.flywaydb.core.Flyway

class GrailsFlywayGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"

    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Grails Flyway 3 Plugin" // Headline display name of the plugin
    def author = "Silvio Wangler"
    def authorEmail = "silvio.wangler@gmail.com"
    def description = '''\
Database migrations using Flyway 3
'''
    def documentation = "https://github.com/saw303/grails-flyway"

    def license = "APACHE"

    def developers = [[name: "Silvio Wangler", email: "silvio.wangler@gmail.com"]]

    def issueManagement = [system: "GITHUB", url: "https://github.com/saw303/grails-flyway/issues"]

    def scm = [url: "https://github.com/saw303/grails-flyway.git"]

    def dependsOn = [hibernate: "4.3 > *"]

    def loadAfter = ['hibernate']

    Closure doWithSpring = {

        if (application.config.flyway.enabled) {

            flyway(Flyway) { bean ->
                bean.initMethod = 'migrate'
                dataSource = ref('dataSource')
                locations = application.config.flyway.locations
                baselineOnMigrate = application.config.flyway.baselineOnMigrate
            }

            def sessionFactoryBeanDef = getBeanDefinition('sessionFactory')

            if (sessionFactoryBeanDef) {
                def dependsOnList = ['flyway']
                if (sessionFactoryBeanDef.dependsOn?.length > 0) dependsOnList.addAll(sessionFactoryBeanDef.dependsOn)
                sessionFactoryBeanDef.dependsOn = dependsOnList as String[]
            }
        }
    }
}
