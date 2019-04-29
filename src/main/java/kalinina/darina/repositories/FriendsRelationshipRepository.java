package kalinina.darina.repositories;

import kalinina.darina.entities.FriendsRelationship;
import kalinina.darina.entities.User;
import org.springframework.data.repository.CrudRepository;

import java.util.Collection;

public interface FriendsRelationshipRepository extends CrudRepository<FriendsRelationship, Long> {
    Collection<FriendsRelationship> findByFromAndConfirmedToAndConfirmedFrom(User user, boolean to, boolean from);
    Collection<FriendsRelationship> findByToAndConfirmedToAndConfirmedFrom(User user, boolean to, boolean from);
    FriendsRelationship findByFromAndTo(User from, User To);
    default Collection<FriendsRelationship> findByUserConfirmed(User user) {
        Collection<FriendsRelationship> collection = findByFromAndConfirmedToAndConfirmedFrom(user, true, true);
        collection.addAll(findByToAndConfirmedToAndConfirmedFrom(user, true, true));
        return collection;
    }
    default Collection<FriendsRelationship> findByUserShouldBeConfirmed(User user) {
        Collection<FriendsRelationship> collection = findByFromAndConfirmedToAndConfirmedFrom(user, true, false);
        collection.addAll(findByToAndConfirmedToAndConfirmedFrom(user, false, true));
        return collection;
    }
    default Collection<FriendsRelationship> findByUserWaitConfirmation(User user) {
        Collection<FriendsRelationship> collection = findByFromAndConfirmedToAndConfirmedFrom(user, false, true);
        collection.addAll(findByToAndConfirmedToAndConfirmedFrom(user, true, false));
        return collection;
    }
    default FriendsRelationship findByUsers(User u1, User u2) {
        FriendsRelationship fr = findByFromAndTo(u1, u2);
        if (fr != null) return fr;
        return findByFromAndTo(u2, u1);
    }
}
