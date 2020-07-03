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

package org.iglooproject.jpa.security.hierarchy;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.acls.domain.PermissionFactory;
import org.springframework.security.acls.model.Permission;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.SetMultimap;


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
public class PermissionHierarchyImpl implements IPermissionHierarchy, Serializable {

	private static final long serialVersionUID = -6242073226861679992L;

	private static final Logger LOGGER = LoggerFactory.getLogger(PermissionHierarchyImpl.class);

    private String permissionHierarchyStringRepresentation = null;

    /**
     * permissionsAcceptableInOneStepMap is a Map that under the key of a specific permission name contains a set of all permissions
     * that can be accepted in place of this permission in 1 step
     */
    private SetMultimap<Permission, Permission> permissionsReachableInOneStepMap = null;

    /**
     * permissionsAcceptableInOneStepMap is a Map that under the key of a specific permission name contains a set of all permissions
     * reachable from this permission in 1 step
     */
    private SetMultimap<Permission, Permission> permissionsAcceptableInOneStepMap = null;

    /**
     * permissionsAcceptableInOneOrMoreStepsMap is a Map that under the key of a specific permission name contains a set of all
     * permissions that can be accepted in place of this permission in 1 or more steps
     */
    private SetMultimap<Permission, Permission> permissionsAcceptableInOneOrMoreStepsMap = null;

    /**
     * permissionsAcceptableInOneOrMoreStepsMap is a Map that under the key of a specific permission name contains a set of all
     * permissions reachable from this permission in 1 or more steps
     */
    private SetMultimap<Permission, Permission> permissionsReachableInOneOrMoreStepsMap = null;
    
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

        buildOneStepRelationsMaps();
        this.permissionsAcceptableInOneOrMoreStepsMap = buildClosures(permissionsAcceptableInOneStepMap);
        this.permissionsReachableInOneOrMoreStepsMap = buildClosures(permissionsReachableInOneStepMap);
    }
    
    @Override
    public List<Permission> getAcceptablePermissions(Permission permission) {
    	return getAcceptablePermissions(ImmutableSet.of(permission));
    }

    @Override
    public List<Permission> getAcceptablePermissions(Collection<Permission> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return new ArrayList<>(0);
        }

        Set<Permission> acceptablePermissions = new HashSet<>();

        for (Permission permission : permissions) {
            acceptablePermissions.add(permission);
            Set<Permission> additionalAcceptablePermissions = permissionsAcceptableInOneOrMoreStepsMap.get(permission);
            if (additionalAcceptablePermissions != null) {
                acceptablePermissions.addAll(additionalAcceptablePermissions);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getAcceptablePermissions() - For one of the permissions " + permissions
                    + " one can accept any of " + acceptablePermissions);
        }

        return new ArrayList<>(acceptablePermissions);
    }
    
    @Override
    public List<Permission> getReachablePermissions(Permission permission) {
    	return getReachablePermissions(ImmutableSet.of(permission));
    }

    @Override
    public List<Permission> getReachablePermissions(Collection<Permission> permissions) {
        if (permissions == null || permissions.size() == 0) {
            return new ArrayList<>(0);
        }

        Set<Permission> reachablePermissions = new HashSet<>();

        for (Permission permission : permissions) {
            reachablePermissions.add(permission);
            Set<Permission> additionalReachablePermissions = permissionsReachableInOneOrMoreStepsMap.get(permission);
            if (additionalReachablePermissions != null) {
                reachablePermissions.addAll(additionalReachablePermissions);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getReachablePermissions() - From the permissions " + permissions
                    + " one can reach " + reachablePermissions + " in zero or more steps.");
        }

        return new ArrayList<>(reachablePermissions);
    }

    /**
     * Parse input and build the map for the permissions reachable in one step: the higher permission will become a key that
     * references a set of the reachable lower permissions.
     */
    private void buildOneStepRelationsMaps() {
        String parsingRegex = "(\\s*([^\\s>]+)\\s*\\>\\s*([^\\s>]+))";
        Pattern pattern = Pattern.compile(parsingRegex);

        Matcher permissionHierarchyMatcher = pattern.matcher(permissionHierarchyStringRepresentation);
        permissionsReachableInOneStepMap = HashMultimap.create();
        permissionsAcceptableInOneStepMap = HashMultimap.create();

        while (permissionHierarchyMatcher.find()) {
            String higherPermissionString = permissionHierarchyMatcher.group(2);
            Permission higherPermission = permissionFactory.buildFromName(higherPermissionString);
            if (higherPermission == null) {
                LOGGER.error(String.format("Unable to build permission %1$s: risk of inconsistent hierarchy", higherPermissionString));
                continue;
            }
            
            String lowerPermissionString = permissionHierarchyMatcher.group(3);
            Permission lowerPermission = permissionFactory.buildFromName(lowerPermissionString);
            if (lowerPermission == null) {
                LOGGER.error(String.format("Unable to build permission %1$s: risk of inconsistent hierarchy", lowerPermissionString));
                continue;
            }
            
            permissionsReachableInOneStepMap.put(higherPermission, lowerPermission);
            permissionsAcceptableInOneStepMap.put(lowerPermission, higherPermission);

            LOGGER.debug("buildPermissionsAcceptableInOneStepMap() - From permission "
                    + higherPermission + " one can reach permission " + lowerPermission + " in one step.");
        }
    }

    private SetMultimap<Permission, Permission> buildClosures(SetMultimap<Permission, Permission> oneStepRelations) {
    	SetMultimap<Permission, Permission> closures = HashMultimap.create();
        // iterate over all higher permissions from permissionsAcceptableInOneStepMap
        Iterator<Permission> permissionIterator = oneStepRelations.keySet().iterator();

        while (permissionIterator.hasNext()) {
            Permission permission = (Permission) permissionIterator.next();
            Set<Permission> permissionsToVisitSet = new HashSet<>();

            if (oneStepRelations.containsKey(permission)) {
                permissionsToVisitSet.addAll(oneStepRelations.get(permission));
            }

            Set<Permission> visitedPermissionsSet = new HashSet<>();

            while (!permissionsToVisitSet.isEmpty()) {
                // take a permission from the permissionsToVisit set
                Permission aPermission = (Permission) permissionsToVisitSet.iterator().next();
                permissionsToVisitSet.remove(aPermission);
                visitedPermissionsSet.add(aPermission);
                if (closures.containsKey(aPermission)) {
                    Set<Permission> newClosure = closures.get(aPermission);

                    // definition of a cycle: you can reach the permission you are starting from
                    if (permissionsToVisitSet.contains(permission) || visitedPermissionsSet.contains(permission)) {
                        throw new CycleInPermissionHierarchyException();
                    } else {
                         // no cycle
                        permissionsToVisitSet.addAll(newClosure);
                    }
                }
            }
        closures.putAll(permission, visitedPermissionsSet);
        }
        
        return closures;

    }

}
