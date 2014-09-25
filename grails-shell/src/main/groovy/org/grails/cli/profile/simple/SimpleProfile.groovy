package org.grails.cli.profile.simple

import groovy.transform.CompileStatic
import jline.console.completer.Completer

import org.grails.cli.CommandLineHandler
import org.grails.cli.profile.Profile

@CompileStatic
class SimpleProfile implements Profile {
    File profileDir
    String name
    List<CommandLineHandler> commandLineHandlers = null
    
    public SimpleProfile(String name, File profileDir) {
        this.name = name
        this.profileDir = profileDir 
    }

    @Override
    public Iterable<Completer> getCompleters() {
        null
    }

    @Override
    public Iterable<CommandLineHandler> getCommandLineHandlers() {
        if(commandLineHandlers == null) {
            commandLineHandlers = []
            File commandsDir = new File(profileDir, "commands")
            Collection<File> yamlFiles = commandsDir.listFiles().findAll { File file ->
                file.isFile() && file.name ==~ /^.*\.yml$/ 
            }.sort(false) { File file -> file.name }
            YamlCommandHandler commandHandler = new YamlCommandHandler(commandFiles: yamlFiles, profile: this)
            commandHandler.initialize()
            commandLineHandlers << commandHandler
        }
        commandLineHandlers
    }    
}
