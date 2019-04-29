package kalinina.darina;

import kalinina.darina.entities.*;
import kalinina.darina.repositories.*;
import kalinina.darina.entities.support.Timetable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Arrays;

@Component
public class Preloader {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private PlatformRepository platformRepository;
    @Autowired
    private FriendsRelationshipRepository friendsRelationshipRepository;
    @Autowired
    private PasswordEncoder encoder;

    @PostConstruct
    public void run(){
//        Arrays.asList(
//                new UserRole(UserRole.Role.ADMIN),
//                new UserRole(UserRole.Role.TEACHER),
//                new UserRole(UserRole.Role.STUDENT),
//                new UserRole(UserRole.Role.PLAYER)
//        ).forEach(role -> userRoleRepository.save(role));
//
//        UserRole player = userRoleRepository.findByRole(UserRole.Role.PLAYER);
//
//        Arrays.asList(
//                new User("Varvara", encoder.encode("password"),
//                        Arrays.asList(
//                                player,
//                                userRoleRepository.findByRole(UserRole.Role.TEACHER),
//                                userRoleRepository.findByRole(UserRole.Role.ADMIN)
//                        )),
//                new User("Vadik", encoder.encode("password"), player),
//                new User("Gosha", encoder.encode("password"), userRoleRepository.findByRole(UserRole.Role.STUDENT)),
//                new User("Darina", encoder.encode("password"), userRoleRepository.findByRole(UserRole.Role.ADMIN))
//        ).forEach(
//                user ->
//                        userRepository.save(user));
//
//        FriendsRelationship fr1 = new FriendsRelationship();
//        fr1.setFrom(userRepository.findUserByLogin("Varvara"));
//        fr1.setTo(userRepository.findUserByLogin("Vadik"));
//        fr1.setConfirmedFrom(true);
//        fr1.setConfirmedTo(true);
//        friendsRelationshipRepository.save(fr1);
//
//        FriendsRelationship fr2 = new FriendsRelationship();
//        fr2.setFrom(userRepository.findUserByLogin("Varvara"));
//        fr2.setTo(userRepository.findUserByLogin("Darina"));
//        fr2.setConfirmedFrom(false);
//        fr2.setConfirmedTo(true);
//        friendsRelationshipRepository.save(fr2);
//
//
//        City msc = new City();
//        msc.setIndex("000000");
//        msc.setName("Москва");
//        msc.setLatitude(55.75);
//        msc.setLongitude(37.62);
//        msc = cityRepository.save(msc);
//
//        City spb = new City();
//        spb.setIndex("190000—199406");
//        spb.setName("Санкт-Петербург");
//        spb.setLatitude(59.94);
//        spb.setLongitude(30.32);
//        spb = cityRepository.save(spb);
//
//
//        Platform platform = new Platform("Спортивная Площадка", 59.899265,30.486031, spb);
//        platform.getTimetable().setMonday(Timetable.closeDay());
//        platform = platformRepository.save(platform);
//
//
//        System.out.println(
//                "\n----------------------------------------------------------\n" +
//                "ПЕРВИЧНЫЕ ДАННЫЕ ЗАГРУЖЕНЫ" +
//                "\n----------------------------------------------------------\n");
    }

}
