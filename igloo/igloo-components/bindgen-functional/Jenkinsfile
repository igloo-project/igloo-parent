@Library('grp.helpers') _

simpleProject {
	config.triggerSetPollSpecEnabled = false
	// stages only on specific project
	config.testEnabled = false
	config.sonarEnabled = true
	config.owaspDependencyCheckEnabled = true
	// deploy automatically snapshots
	config.deployEnabled = true
	config.gitlabEnabled = false
	config.githubEnabled = true
	config.githubUrl = 'https://github.com/igloo-project/bindgen-java/'

	// jenkins choose to poll once by 3 hours
	config.triggerSetPollSpecCronExpression = 'H H/3 * * *'
	config.notificationRecipients = 'grp-jenkins@lists.projects.openwide.fr'
	config.buildBlockerSimpleLock = 'bindgen.*'
	// build use toolchains to handle jdk 7 builds
	config.jdk = 'JDK 1.8'
	config.buildTarget = 'install'
	config.defaultMavenArgs = '-Ddistribution=owsi-core-release -DperformRelease -Dmaven.javadoc.skip -Dmaven.repo.local="${WORKSPACE}/m2-repository/"'
	config.beforeNotification = {
		util_sh 'rm -rf "${WORKSPACE}/m2-repository/"'
	}
}
