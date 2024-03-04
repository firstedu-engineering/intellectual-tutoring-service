
CREATE TABLE `student_practice_history` (
  `id` char(36) NOT NULL,
  `student_id` char(36) NOT NULL,
  `sub_content_id` char(36) NOT NULL,
  `question_id` char(36) NOT NULL,
  `correct` TINYINT(1) NOT NULL,
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
