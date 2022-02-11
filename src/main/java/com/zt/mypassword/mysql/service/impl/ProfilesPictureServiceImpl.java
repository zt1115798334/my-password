package com.zt.mypassword.mysql.service.impl;

import com.zt.mypassword.exception.custom.OperationException;
import com.zt.mypassword.mysql.entity.ProfilesPicture;
import com.zt.mypassword.mysql.repo.ProfilesPictureRepository;
import com.zt.mypassword.mysql.service.ProfilesPictureService;
import com.zt.mypassword.properties.FileProperties;
import com.zt.mypassword.utils.FileUtils;
import com.zt.mypassword.enums.DeleteState;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class ProfilesPictureServiceImpl implements ProfilesPictureService {

    private final FileProperties fileProperties;

    private final ProfilesPictureRepository profilesPictureRepository;

    @Override
    public ProfilesPicture saveProfilesPicture(MultipartFile multipartFile) {
        String diskPath = FileUtils.writeDisk(multipartFile, Paths.get(fileProperties.getProfilesPicturePath()));
        ProfilesPicture profilesPicture = ProfilesPicture.builder()
                .path(diskPath)
                .deleteState(DeleteState.UN_DELETE)
                .createdTime(LocalDateTime.now()).build();
        return profilesPictureRepository.save(profilesPicture);
    }

    @Override
    public ProfilesPicture findProfilesPicture(Long id) {
        return profilesPictureRepository.findById(id).orElseThrow(() -> new OperationException("不存在"));
    }
}
