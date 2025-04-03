package com.verby.indp.domain.store.service;


import com.verby.indp.domain.common.exception.NotFoundException;
import com.verby.indp.domain.region.Region;
import com.verby.indp.domain.region.repository.RegionRepository;
import com.verby.indp.domain.region.vo.RegionName;
import com.verby.indp.domain.song.SongForm;
import com.verby.indp.domain.song.repository.SongFormRepository;
import com.verby.indp.domain.song.vo.SongFormName;
import com.verby.indp.domain.store.Store;
import com.verby.indp.domain.store.dto.request.AddStoreByAdminRequest;
import com.verby.indp.domain.store.dto.request.UpdateStoreByAdminRequest;
import com.verby.indp.domain.store.dto.response.FindSimpleStoresResponse;
import com.verby.indp.domain.store.dto.response.FindStoreByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresByAdminResponse;
import com.verby.indp.domain.store.dto.response.FindStoresResponse;
import com.verby.indp.domain.store.repository.StoreRepository;
import com.verby.indp.domain.theme.Theme;
import com.verby.indp.domain.theme.repository.ThemeRepository;
import com.verby.indp.domain.theme.vo.ThemeName;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class StoreService {

    private final StoreRepository storeRepository;
    private final ThemeRepository themeRepository;
    private final SongFormRepository songFormRepository;
    private final RegionRepository regionRepository;

    public FindSimpleStoresResponse findSimpleStores(Pageable pageable) {
        Page<Store> page = storeRepository.findAllByOrderByStoreIdAsc(pageable);

        return FindSimpleStoresResponse.from(page);
    }

    public FindStoresResponse findStores(Pageable pageable, String regionName) {
        if (regionName == null) {
            Page<Store> page = storeRepository.findAllByOrderByStoreIdAsc(
                pageable);

            return FindStoresResponse.from(page);
        }

        Region region = getRegionByName(regionName);
        Page<Store> page = storeRepository.findAllByRegionOrderByStoreIdAsc(pageable, region);

        return FindStoresResponse.from(page);
    }

    public FindStoresByAdminResponse findStoresByAdmin(Pageable pageable) {
        Page<Store> page = storeRepository.findAllByOrderByStoreIdAsc(
            pageable);

        return FindStoresByAdminResponse.from(page);
    }

    public FindStoreByAdminResponse findStoreByAdmin(long storeId) {
        Store store = getStoreById(storeId);
        return FindStoreByAdminResponse.from(store);
    }

    @Transactional
    public void deleteStoreByAdmin(Long storeId) {
        storeRepository.deleteById(storeId);
    }

    @Transactional
    public long addStore(AddStoreByAdminRequest request) {
        Store store = new Store(
            request.name(),
            request.address(),
            getRegion(request.region()),
            List.of(request.imageUrl()),
            getThemes(request.themes()),
            getSongForms(request.songForms())
        );
        Store saveStore = storeRepository.save(store);
        return saveStore.getStoreId();
    }

    @Transactional
    public void updateStore(Long storeId, UpdateStoreByAdminRequest request) {
        Store store = getStoreById(storeId);
        store.update(
            request.name(),
            request.address(),
            getRegion(request.region()),
            List.of(request.imageUrl()),
            getThemes(request.themes()),
            getSongForms(request.songForms())
        );
    }

    private List<SongForm> getSongForms(List<String> names) {
        ArrayList<SongForm> songForms = new ArrayList<>();
        names.forEach(name -> {
            SongForm songForm = songFormRepository.findByName(new SongFormName(name))
                .orElseGet(() -> songFormRepository.save(new SongForm(name)));

            songForms.add(songForm);
        });

        return songForms;

    }

    private ArrayList<Theme> getThemes(List<String> names) {
        ArrayList<Theme> themes = new ArrayList<>();
        names.forEach(name -> {
            Theme theme = themeRepository.findByName(new ThemeName(name))
                .orElseGet(() -> themeRepository.save(new Theme(name)));

            themes.add(theme);
        });

        return themes;
    }

    private Region getRegion(String name) {
        return regionRepository.findByName(new RegionName(name))
            .orElseGet(() -> regionRepository.save(new Region(name)));
    }

    private Store getStoreById(long storeId) {
        return storeRepository.findById(storeId)
            .orElseThrow(() -> new NotFoundException("존재하지 않는 매장입니다."));
    }

    private Region getRegionByName(String name) {
        return regionRepository.findByName(new RegionName(name))
            .orElseThrow(() -> new NotFoundException("등록되지 않은 지역입니다."));
    }
}
