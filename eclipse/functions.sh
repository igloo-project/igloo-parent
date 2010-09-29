#! /bin/bash

function install_plugins() {
	path_eclipse=`readlink -e "$1"`
	features="$2"
	repositories="$3"

	correct_library_path="-Djava.library.path=/usr/lib:/usr/lib/jni"

	${path_eclipse}/eclipse -nosplash \
		-application org.eclipse.equinox.p2.director \
		-repository "${repositories}" \
		-installIU "${features}"
	
	library_path=`grep -r "java.library.path" ${path_eclipse}/eclipse.ini`
	
	if [ "${library_path}" != "${correct_library_path}" ]; then 
		echo "${correct_library_path}" >> ${path_eclipse}/eclipse.ini
	fi
}
