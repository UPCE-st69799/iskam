package upce.cz.iskam.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import upce.cz.iskam.entity.AppUser;
import upce.cz.iskam.repository.AppUserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class AppUserService {
    private final AppUserRepository appUserRepository;

    @Transactional(readOnly = true)
    public List<AppUser> findAllByActiveEquals() {
        return appUserRepository.findAllByActiveEquals(true);
    }

    @Transactional(readOnly = true)
    public AppUser findById(Long id) throws ResourceNotFoundException {
        var result = appUserRepository.findById(id);

        if (result.isEmpty()) {
            throw new ResourceNotFoundException();
        }

        return result.get();
    }

    @Transactional
    public AppUser create(final AppUser appUser) {
        return appUserRepository.save(appUser);
    }

    @Transactional
    public AppUser update(final AppUser toEntity) {
        return appUserRepository.save(toEntity);
    }

    @Transactional
    public void delete(final Long id) {
        appUserRepository.deleteById(id);
    }
}