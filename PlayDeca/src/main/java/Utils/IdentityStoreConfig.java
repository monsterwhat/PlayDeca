package Utils;

import jakarta.security.enterprise.identitystore.DatabaseIdentityStoreDefinition;

/**
 *
 * @author Al
 */
public class IdentityStoreConfig {
    @DatabaseIdentityStoreDefinition(
        dataSourceLookup = "jdbc/MySQL",
        callerQuery = "select password from Users where username = ?",
        groupsQuery = "select userGroup from Users where username = ?"
    )
    public static class IdentityStoreConfigProvider {}
    
}
