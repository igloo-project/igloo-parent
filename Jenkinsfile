@Library('grp.helpers') _

simpleProject {
	def isTestEnabled = env.JOB_NAME.contains('-tests')
	config.triggerSetPollSpecEnabled = isTestEnabled
	// stages only on specific project
	config.testEnabled = isTestEnabled
	config.sonarEnabled = true
	config.owaspDependencyCheckEnabled = env.JOB_NAME.contains('-owasp')
	config.deployEnabled = true
	config.gitlabEnabled = false
	config.githubEnabled = true
	config.githubUrl = 'https://github.com/igloo-project/igloo-parent/'

	// jenkins choose to poll once by 3 hours
	config.triggerSetPollSpecCronExpression = 'H H/3 * * *'
	config.notificationRecipients = 'grp-jenkins@lists.projects.openwide.fr'
	config.buildBlockerSimpleLock = 'igloo.*'
	// igloo prerequisites: java 8
	config.jdk = 'JDK 1.8'
	config.buildTarget = 'install'
	config.defaultMavenArgs = '-Ptest -Ddistribution=igloo-release -Dmaven.repo.local="${WORKSPACE}/m2-repository/"'
	config.deployMavenArgs = '-DperformRelease=true -Dmaven.javadoc.skip=true'
	config.beforeNotification = {
		util_sh 'rm -rf "${WORKSPACE}/m2-repository/"'
	}
}
