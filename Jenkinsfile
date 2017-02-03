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
	config.githubUrl = 'https://github.com/openwide-java/owsi-core-parent/'

	// jenkins choose to poll once by 3 hours
	config.triggerSetPollSpecCronExpression = 'H H/3 * * *'
	config.notificationRecipients = 'grp-jenkins@lists.projects.openwide.fr'
	config.buildBlockerSimpleLock = 'owsi-core.*'
	// build use toolchains to handle jdk 7 builds
	config.jdk = 'JDK 1.7'
	config.buildTarget = 'install'
	config.defaultMavenArgs = '-Dmanipulation.disable=true -Ptest -Ddistribution=owsi-core-release -Dmaven.repo.local="${WORKSPACE}/m2-repository/"'
	config.beforeNotification = {
		util_sh 'rm -rf "${WORKSPACE}/m2-repository/"'
	}
}
