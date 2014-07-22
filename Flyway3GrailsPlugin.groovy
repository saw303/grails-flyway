import org.flywaydb.core.Flyway

class Flyway3GrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.4 > *"
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def title = "Flyway Plugin" // Headline display name of the plugin
    def author = "Silvio Wangler"
    def authorEmail = "silvio.wangler@gmail.com"
    def description = '''\
simple straight forward flyway plugin.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/flyway3"
    def license = "APACHE"

    // Details of company behind the plugin (if there is one)
    def organization = [name: "Silvio", url: "http://www.wangler.io/"]

    // Any additional developers beyond the author specified above.
    def developers = [[name: "Silvio Wangler", email: "silvio.wangler@gmail.com"]]

    // Location of the plugin's issue tracker.
//    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPMYPLUGIN" ]

    // Online location of the plugin's browseable source code.
//    def scm = [ url: "http://svn.codehaus.org/grails-plugins/" ]

    def loadAfter = ['hibernate4']

    def doWithSpring = {

        if (application.config.flyway.enabled) {

            flyway(Flyway) { bean ->
                bean.initMethod = 'migrate'
                dataSource = ref('dataSource')
                locations = application.config.flyway.locations
                initOnMigrate = application.config.flyway.initOnMigrate
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
