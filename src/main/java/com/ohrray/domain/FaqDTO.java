package com.ohrray.domain;


import com.ohrray.entity.Faq;
import lombok.*;


@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FaqDTO {
    private Long id;
    private String title;
    private String content;


    public static FaqDTO convertToDTO(Faq faq) {
        return FaqDTO.builder()
                .id(faq.getId())
                .title(faq.getTitle())
                .content(faq.getContent())
                .build();
    }

    public static Faq convertToEntity(FaqDTO faqDTO) {
        Faq faq = new Faq();
        faq.setId(faqDTO.getId());
        faq.setTitle(faqDTO.getTitle());
        faq.setContent(faqDTO.getContent());
        return faq;
    }


}
