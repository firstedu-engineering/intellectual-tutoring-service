
CREATE TABLE `student_practice` (
  `id` char(36) NOT NULL,
  `student_id` char(36) NOT NULL,
  `sub_content_id` char(36) NOT NULL,
  `question_id` char(36) NOT NULL,
  `question_body` text NOT NULL,
  `question_options` JSON NOT NULL,
  `question_selected_option_id` char(36),
  `question_correct` TINYINT(1),
  `question_submitted` TINYINT(1) NOT NULL,
  `question_recommended_reason` varchar(200),
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
