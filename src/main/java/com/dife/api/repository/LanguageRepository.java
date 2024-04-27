package com.dife.api.repository;

import com.dife.api.model.Language;
import com.dife.api.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LanguageRepository extends JpaRepository<Language, Long> {

    Optional<Language> findByMemberAndName(@Param("member") Member member, @Param("languageName") String languageName);
}
