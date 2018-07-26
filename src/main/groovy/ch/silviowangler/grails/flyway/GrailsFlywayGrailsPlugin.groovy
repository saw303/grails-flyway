package ch.silviowangler.grails.flyway

import grails.plugins.Plugin
import org.flywaydb.core.Flyway
import org.springframework.beans.factory.config.BeanDefinition

class GrailsFlywayGrailsPlugin extends Plugin {

    def grailsVersion = "3.0.0 > *"
    def title = "Grails Flyway 3 Plugin"
    def author = "Silvio Wangler"
    def authorEmail = "silvio.wangler@gmail.com"
    def description = 'Database migrations using Flyway 3'
    def documentation = "https://github.com/saw303/grails-flyway"
    def license = "APACHE"
    def developers = [[name: "Silvio Wangler", email: "silvio.wangler@gmail.com"]]
    def issueManagement = [system: "GITHUB", url: "https://github.com/saw303/grails-flyway/issues"]
    def scm = [url: "https://github.com/saw303/grails-flyway.git"]
    def loadAfter = ['hibernate']

    Closure doWithSpring() {
        { ->
            if (application.config.flyway.enabled) {

                flyway(Flyway) { bean ->
                    bean.initMethod = 'migrate'

                    application.config.flyway.each {k, v ->
                        if(!"enabled".equalsIgnoreCase(k)) {
                            flyway."${k}" = v
                        }
                    }

                    dataSource = ref('dataSource')
                }

                BeanDefinition sessionFactoryBeanDef = getBeanDefinition('sessionFactory')

                if (sessionFactoryBeanDef) {
                    def dependsOnList = ['flyway'] as Set
                    if (sessionFactoryBeanDef.dependsOn?.length > 0) {
                        dependsOnList.addAll(sessionFactoryBeanDef.dependsOn)
                    }
                    sessionFactoryBeanDef.dependsOn = dependsOnList as String[]
                }
            }
            else {
                log.info "Grails Flyway plugin has been disabled"
            }
        }
    }
}
