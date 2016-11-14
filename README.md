# LIFX bulbs as build notifier

[![Coverage Status](https://coveralls.io/repos/github/jenkinsci/lifx-notifier-plugin/badge.svg?branch=master)](https://coveralls.io/github/jenkinsci/lifx-notifier-plugin?branch=master) 
[![wiki](https://img.shields.io/badge/Lifx%20notifier%20plugin-wiki-blue.svg?style=flat)](https://wiki.jenkins-ci.org/display/JENKINS/LIFX+notifier+plugin)

Install this plugin and be dazzled. 
It will set it red when bad, green when good, pale white when building.
Colors can be changed in job configuration.

<img src="https://wiki.jenkins-ci.org/download/attachments/73532169/Screen+Shot+2014-07-14+at+7.17.09+pm.png?version=1&modificationDate=1405329559000"/>


# To run from source

```
mvn hpi:run
```

This will discover lights on your network - if you have problems, ensure you can access lights from the iphone/android app.

The Jenkins <a href="https://wiki.jenkins-ci.org/display/JENKINS/LIFX+notifier+plugin">wiki</a> page for this.