/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.openwide.core.hibernate.security.acl.domain.hierarchy;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;


/**
 * <p>
 * This class defines a permission hierarchy for use with the UserDetailsServiceWrapper.
 * </p>
 * <p>
 * Here is an example configuration of a permission hierarchy (hint: read the "&gt;" sign as "includes"):
<pre>
        &lt;property name="hierarchy"&gt;
            &lt;value&gt;
                ROLE_A &gt; ROLE_B
                ROLE_B &gt; ROLE_AUTHENTICATED
                ROLE_AUTHENTICATED &gt; ROLE_UNAUTHENTICATED
            &lt;/value&gt;
        &lt;/property&gt;
</pre>
</p>
 * <p>
 * Explanation of the above:<br>
 * In effect every user with ROLE_A also has ROLE_B, ROLE_AUTHENTICATED and ROLE_UNAUTHENTICATED;<br>
 * every user with ROLE_B also has ROLE_AUTHENTICATED and ROLE_UNAUTHENTICATED;<br>
 * every user with ROLE_AUTHENTICATED also has ROLE_UNAUTHENTICATED.
 * </p>
 * <p>
 * Hierarchical Permissions will dramatically shorten your access rules (and also make the access rules much more elegant).
 * </p>
 * <p>
 * Consider this access rule for Spring Security's PermissionVoter (background: every user that is authenticated should be
 * able to log out):<br>
 * /logout.html=ROLE_A,ROLE_B,ROLE_AUTHENTICATED<br>
 * With hierarchical permissions this can now be shortened to:<br>
 * /logout.html=ROLE_AUTHENTICATED<br>
 * In addition to shorter rules this will also make your access rules more readable and your intentions clearer.
 * </p>
 *
 * @author Michael Mayr
 *
 */
public class PermissionHierarchyImpl implements PermissionHierarchy, Serializable {

	private static final long serialVersionUID = -6242073226861679992L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionHierarchyImpl.class);

    private String permissionHierarchyStringRepresentation = null;

    /**
     * permissionsAcceptableInOneStepMap is a Map that under the key of a specific permission name contains a set of all permissions
     * acceptable from this permission in 1 step
     */
    private Map<Permission, Set<Permission>> permissionsAcceptableInOneStepMap = null;

    /**
     * permissionsAcceptableInOneOrMoreStepsMap is a Map that under the key of a specific permission name contains a set of all
     * permissions acceptable from this permission in 1 or more steps
     */
    private Map<Permission, Set<Permission>> permissionsAcceptableInOneOrMoreStepsMap = null;
    
    private PermissionFactory permissionFactory;
    

    public PermissionHierarchyImpl(PermissionFactory permissionFactory) {
    	this.permissionFactory = permissionFactory;
    }

    /**
     * Set the permission hierarchy and precalculate for every permission the set of all acceptable permissions, i. e. all permissions lower in
     * the hierarchy of every given permission. Precalculation is done for performance reasons (acceptable permissions can then be
     * calculated in O(1) time).
     * During precalculation cycles in permission hierarchy are detected and will cause a
     * <tt>CycleInPermissionHierarchyException</tt> to be thrown.
     *
     * @param permissionHierarchyStringRepresentation - String definition of the permission hierarchy.
     */
    public void setHierarchy(String permissionHierarchyStringRepresentation) {
        this.permissionHierarchyStringRepresentation = permissionHierarchyStringRepresentation;

        LOGGER.debug("setHierarchy() - The following permission hierarchy was set: " + permissionHierarchyStringRepresentation);

        buildPermissionsAcceptableInOneStepMap();
        buildPermissionsAcceptableInOneOrMoreStepsMap();
    }
    
    public List<Permission> getAcceptablePermissions(Permission permission) {
    	Collection<Permission> permissions = new ArrayList<Permission>(1);
    	permissions.add(permission);
    	
    	return getAcceptablePermissions(permissions);
    }

    public List<Permission> getAcceptablePermissions(Collection<Permission> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return null;
        }

        Set<Permission> acceptablePermissions = new HashSet<Permission>();

        for (Permission permission : permissions) {
            acceptablePermissions.add(permission);
            Set<Permission> additionalAcceptablePermissions = permissionsAcceptableInOneOrMoreStepsMap.get(permission);
            if (additionalAcceptablePermissions != null) {
                acceptablePermissions.addAll(additionalAcceptablePermissions);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getAcceptablePermissions() - From the permissions " + permissions
                    + " one can reach " + acceptablePermissions + " in zero or more steps.");
        }

        return new ArrayList<Permission>(acceptablePermissions);
    }

    /**
     * Parse input and build the map for the permissions acceptable in one step: the higher permission will become a key that
     * references a set of the acceptable lower permissions.
     */
    private void buildPermissionsAcceptableInOneStepMap() {
        String parsingRegex = "(\\s*([^\\s>]+)\\s*\\>\\s*([^\\s>]+))";
        Pattern pattern = Pattern.compile(parsingRegex);

        Matcher permissionHierarchyMatcher = pattern.matcher(permissionHierarchyStringRepresentation);
        permissionsAcceptableInOneStepMap = new HashMap<Permission, Set<Permission>>();

        while (permissionHierarchyMatcher.find()) {
            Permission higherPermission = permissionFactory.buildFromName(permissionHierarchyMatcher.group(2));
            Permission lowerPermission = permissionFactory.buildFromName(permissionHierarchyMatcher.group(3));
            Set<Permission> permissionsAcceptableInOneStepSet = null;

            if (!permissionsAcceptableInOneStepMap.containsKey(lowerPermission)) {
                permissionsAcceptableInOneStepSet = new HashSet<Permission>();
                permissionsAcceptableInOneStepMap.put(lowerPermission, permissionsAcceptableInOneStepSet);
            } else {
                permissionsAcceptableInOneStepSet = permissionsAcceptableInOneStepMap.get(lowerPermission);
            }
            permissionsAcceptableInOneStepSet.add(higherPermission);

            LOGGER.debug("buildPermissionsAcceptableInOneStepMap() - From permission "
                    + lowerPermission + " one can reach permission " + higherPermission + " in one step.");
        }
    }

    /**
     * For every higher permission from permissionsAcceptableInOneStepMap store all permissions that are acceptable from it in the map of
     * permissions acceptable in one or more steps. (Or throw a CycleInPermissionHierarchyException if a cycle in the permission
     * hierarchy definition is detected)
     */
    private void buildPermissionsAcceptableInOneOrMoreStepsMap() {
        permissionsAcceptableInOneOrMoreStepsMap = new HashMap<Permission, Set<Permission>>();
        // iterate over all higher permissions from permissionsAcceptableInOneStepMap
        Iterator<Permission> permissionIterator = permissionsAcceptableInOneStepMap.keySet().iterator();

        while (permissionIterator.hasNext()) {
            Permission permission = (Permission) permissionIterator.next();
            Set<Permission> permissionsToVisitSet = new HashSet<Permission>();

            if (permissionsAcceptableInOneStepMap.containsKey(permission)) {
                permissionsToVisitSet.addAll(permissionsAcceptableInOneStepMap.get(permission));
            }

            Set<Permission> visitedPermissionsSet = new HashSet<Permission>();

            while (!permissionsToVisitSet.isEmpty()) {
                // take a permission from the permissionsToVisit set
                Permission aPermission = (Permission) permissionsToVisitSet.iterator().next();
                permissionsToVisitSet.remove(aPermission);
                visitedPermissionsSet.add(aPermission);
                if (permissionsAcceptableInOneStepMap.containsKey(aPermission)) {
                    Set<Permission> newAcceptablePermissions = (Set<Permission>) permissionsAcceptableInOneStepMap.get(aPermission);

                    // definition of a cycle: you can reach the permission you are starting from
                    if (permissionsToVisitSet.contains(permission) || visitedPermissionsSet.contains(permission)) {
                        throw new CycleInPermissionHierarchyException();
                    } else {
                         // no cycle
                        permissionsToVisitSet.addAll(newAcceptablePermissions);
                    }
                }
            }
            permissionsAcceptableInOneOrMoreStepsMap.put(permission, visitedPermissionsSet);

            LOGGER.debug("buildPermissionsAcceptableInOneOrMoreStepsMap() - From permission "
                    + permission + " one can reach " + visitedPermissionsSet + " in one or more steps.");
        }

    }

}
