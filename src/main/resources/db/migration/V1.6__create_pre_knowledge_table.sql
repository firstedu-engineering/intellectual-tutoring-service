
CREATE TABLE `pre_knowledge_relation` (
  `id` char(36) NOT NULL,
  `knowledge_point_id` char(36) NOT NULL,
  `pre_knowledge_point_id` char(36) NOT NULL,
  `relevance` decimal(6,2) NOT NULL,
  `created_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at`      TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
