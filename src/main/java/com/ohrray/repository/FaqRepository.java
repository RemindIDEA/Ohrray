package com.ohrray.repository;

import com.ohrray.entity.Faq;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FaqRepository extends JpaRepository<Faq, Long> {
}
