#! /bin/bash

function install_plugins() {
	path_eclipse=`readlink -e "$1"`
	features="$2"
	repositories="$3"

	${path_eclipse}/eclipse -nosplash \
		-application org.eclipse.equinox.p2.director \
		-repository "${repositories}" \
		-installIU "${features}"
}
