package com.divforce.cr.apigateway.users;

import lombok.Data;

import java.util.List;

/**
 * @author Oscar Makala
 */
@Data
public class GroupDtoPage {
    private List<GroupDto> resources;
    private Long startIndex;
    private Long itemsPerPage;
    private Long totalResults;
}
