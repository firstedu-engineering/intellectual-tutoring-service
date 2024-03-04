CREATE TABLE `student_knowledge_ability` (
  `id` char(36) NOT NULL,
  `student_id` char(36) NOT NULL,
  `sub_content_id` char(36) NOT NULL,
  `current_score` INT NOT NULL,
  `target_score` INT NOT NULL,
  `base_score` INT NOT NULL,
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
