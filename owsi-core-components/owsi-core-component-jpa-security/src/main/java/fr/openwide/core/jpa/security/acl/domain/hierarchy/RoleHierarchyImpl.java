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

package fr.openwide.core.jpa.security.acl.domain.hierarchy;


import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.hierarchicalroles.CycleInRoleHierarchyException;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthorityImpl;

/**
 * OW: cette classe a été extraite de spring-security pour implémenter Serializable ;
 * ceci permet au mécanisme de session de sérialiser sans erreur cette classe.
 * Le bénéfice direct de ceci est :
 * <ul>
 *   <li>pas de perte de session à l'arrêt/démarrage de tomcat</li>
 *   <li>pas de message d'erreur dans la récupération d'une session sérialisée</li>
 * </ul>
 * TODO: spring-security prévoit de modifier le fonctionnement pour que cet objet
 * ne fasse plus partie de la session ; si passage à spring 2.0.4, le mode de fonctionnement
 * n'imposera plus le Serializable (http://jira.springframework.org/browse/SEC-869).
 * <p>
 * This class defines a role hierarchy for use with the UserDetailsServiceWrapper.
 * </p>
 * <p>
 * Here is an example configuration of a role hierarchy (hint: read the "&gt;" sign as "includes"):
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
 * Hierarchical Roles will dramatically shorten your access rules (and also make the access rules much more elegant).
 * </p>
 * <p>
 * Consider this access rule for Spring Security's RoleVoter (background: every user that is authenticated should be
 * able to log out):<br>
 * /logout.html=ROLE_A,ROLE_B,ROLE_AUTHENTICATED<br>
 * With hierarchical roles this can now be shortened to:<br>
 * /logout.html=ROLE_AUTHENTICATED<br>
 * In addition to shorter rules this will also make your access rules more readable and your intentions clearer.
 * </p>
 *
 * @author Michael Mayr
 *
 */
public class RoleHierarchyImpl implements RoleHierarchy, Serializable {

	private static final long serialVersionUID = -6242073226861679992L;

	private static final Logger LOGGER = LoggerFactory.getLogger(RoleHierarchyImpl.class);

    private String roleHierarchyStringRepresentation = null;

    /**
     * rolesReachableInOneStepMap is a Map that under the key of a specific role name contains a set of all roles
     * reachable from this role in 1 step
     */
    private Map<GrantedAuthority, Set<GrantedAuthority>> rolesReachableInOneStepMap = null;

    /**
     * rolesReachableInOneOrMoreStepsMap is a Map that under the key of a specific role name contains a set of all
     * roles reachable from this role in 1 or more steps
     */
    private Map<GrantedAuthority, Set<GrantedAuthority>> rolesReachableInOneOrMoreStepsMap = null;

    /**
     * Set the role hierarchy and precalculate for every role the set of all reachable roles, i. e. all roles lower in
     * the hierarchy of every given role. Precalculation is done for performance reasons (reachable roles can then be
     * calculated in O(1) time).
     * During precalculation cycles in role hierarchy are detected and will cause a
     * <tt>CycleInRoleHierarchyException</tt> to be thrown.
     *
     * @param roleHierarchyStringRepresentation - String definition of the role hierarchy.
     */
    public void setHierarchy(String roleHierarchyStringRepresentation) {
        this.roleHierarchyStringRepresentation = roleHierarchyStringRepresentation;

        LOGGER.debug("setHierarchy() - The following role hierarchy was set: " + roleHierarchyStringRepresentation);

        buildRolesReachableInOneStepMap();
        buildRolesReachableInOneOrMoreStepsMap();
    }

    public Collection<GrantedAuthority> getReachableGrantedAuthorities(Collection<GrantedAuthority> authorities) {
        if (authorities == null || authorities.size() == 0) {
            return null;
        }

        Set<GrantedAuthority> reachableRoles = new HashSet<GrantedAuthority>();

        for (GrantedAuthority authority : authorities) {
            reachableRoles.add(authority);
            Set<GrantedAuthority> additionalReachableRoles = rolesReachableInOneOrMoreStepsMap.get(authority);
            if (additionalReachableRoles != null) {
                reachableRoles.addAll(additionalReachableRoles);
            }
        }

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("getReachableGrantedAuthorities() - From the roles " + authorities
                    + " one can reach " + reachableRoles + " in zero or more steps.");
        }

        return reachableRoles;
    }

    /**
     * Parse input and build the map for the roles reachable in one step: the higher role will become a key that
     * references a set of the reachable lower roles.
     */
    private void buildRolesReachableInOneStepMap() {
        String parsingRegex = "(\\s*([^\\s>]+)\\s*\\>\\s*([^\\s>]+))";
        Pattern pattern = Pattern.compile(parsingRegex);

        Matcher roleHierarchyMatcher = pattern.matcher(roleHierarchyStringRepresentation);
        rolesReachableInOneStepMap = new HashMap<GrantedAuthority, Set<GrantedAuthority>>();

        while (roleHierarchyMatcher.find()) {
            GrantedAuthority higherRole = new GrantedAuthorityImpl(roleHierarchyMatcher.group(2));
            GrantedAuthority lowerRole = new GrantedAuthorityImpl(roleHierarchyMatcher.group(3));
            Set<GrantedAuthority> rolesReachableInOneStepSet = null;

            if (!rolesReachableInOneStepMap.containsKey(higherRole)) {
                rolesReachableInOneStepSet = new HashSet<GrantedAuthority>();
                rolesReachableInOneStepMap.put(higherRole, rolesReachableInOneStepSet);
            } else {
                rolesReachableInOneStepSet = rolesReachableInOneStepMap.get(higherRole);
            }
            rolesReachableInOneStepSet.add(lowerRole);

            LOGGER.debug("buildRolesReachableInOneStepMap() - From role "
                    + higherRole + " one can reach role " + lowerRole + " in one step.");
        }
    }

    /**
     * For every higher role from rolesReachableInOneStepMap store all roles that are reachable from it in the map of
     * roles reachable in one or more steps. (Or throw a CycleInRoleHierarchyException if a cycle in the role
     * hierarchy definition is detected)
     */
    private void buildRolesReachableInOneOrMoreStepsMap() {
        rolesReachableInOneOrMoreStepsMap = new HashMap<GrantedAuthority, Set<GrantedAuthority>>();
        // iterate over all higher roles from rolesReachableInOneStepMap
        Iterator<GrantedAuthority> roleIterator = rolesReachableInOneStepMap.keySet().iterator();

        while (roleIterator.hasNext()) {
            GrantedAuthority role = (GrantedAuthority) roleIterator.next();
            Set<GrantedAuthority> rolesToVisitSet = new HashSet<GrantedAuthority>();

            if (rolesReachableInOneStepMap.containsKey(role)) {
                rolesToVisitSet.addAll(rolesReachableInOneStepMap.get(role));
            }

            Set<GrantedAuthority> visitedRolesSet = new HashSet<GrantedAuthority>();

            while (!rolesToVisitSet.isEmpty()) {
                // take a role from the rolesToVisit set
                GrantedAuthority aRole = (GrantedAuthority) rolesToVisitSet.iterator().next();
                rolesToVisitSet.remove(aRole);
                visitedRolesSet.add(aRole);
                if (rolesReachableInOneStepMap.containsKey(aRole)) {
                    Set<GrantedAuthority> newReachableRoles = (Set<GrantedAuthority>) rolesReachableInOneStepMap.get(aRole);

                    // definition of a cycle: you can reach the role you are starting from
                    if (rolesToVisitSet.contains(role) || visitedRolesSet.contains(role)) {
                        throw new CycleInRoleHierarchyException();
                    } else {
                         // no cycle
                        rolesToVisitSet.addAll(newReachableRoles);
                    }
                }
            }
            rolesReachableInOneOrMoreStepsMap.put(role, visitedRolesSet);

            LOGGER.debug("buildRolesReachableInOneOrMoreStepsMap() - From role "
                    + role + " one can reach " + visitedRolesSet + " in one or more steps.");
        }

    }

}
