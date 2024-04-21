package org.example.musicstream.dtos.response.api;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PageResponse<T>{
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private T items;
}
