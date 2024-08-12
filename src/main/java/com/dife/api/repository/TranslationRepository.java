package com.dife.api.repository;

import com.dife.api.model.Bookmark;
import com.dife.api.model.Translation;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TranslationRepository extends JpaRepository<Translation, Long> {

	List<Translation> findAllByBookmarks(List<Bookmark> bookmarks);
}
