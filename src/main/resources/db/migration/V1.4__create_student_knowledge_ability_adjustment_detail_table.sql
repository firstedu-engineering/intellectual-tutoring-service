
CREATE TABLE `student_knowledge_ability_adjustment_detail` (
  `id` char(36) NOT NULL,
  `adjustment_id` char(36) NOT NULL,
  `sub_content_id` char(36) NOT NULL,
  `before_adjustment_score` decimal(6,2) NOT NULL,
  `after_adjustment_score` decimal(6,2) NOT NULL,
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
