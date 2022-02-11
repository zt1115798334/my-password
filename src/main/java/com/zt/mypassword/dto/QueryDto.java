package com.zt.mypassword.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.zt.mypassword.enums.SortField;
import com.zt.mypassword.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/9/22 9:58
 * description:
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryDto extends PageDto {

    private SortField sortField = SortField.publishTime;

    private Sort.Direction sortOrder = Sort.Direction.DESC;

    @DateTimeFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime startDateTime;

    @DateTimeFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    @JsonFormat(pattern = DateUtils.DATE_TIME_FORMAT)
    private LocalDateTime endDateTime;
}


