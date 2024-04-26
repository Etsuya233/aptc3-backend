-- --------------------------------------------------------
-- 主机:                           localhost
-- 服务器版本:                        8.0.27 - MySQL Community Server - GPL
-- 服务器操作系统:                      Linux
-- HeidiSQL 版本:                  12.6.0.6765
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 aptc 的数据库结构
CREATE DATABASE IF NOT EXISTS `aptc` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `aptc`;

-- 导出  表 aptc.t_score 结构
DROP TABLE if exists `t_score`;
CREATE TABLE IF NOT EXISTS `t_score` (
  `scid` int NOT NULL AUTO_INCREMENT,
  `uid` int NOT NULL,
  `sid` int NOT NULL,
  `pst_score` int DEFAULT NULL,
  `pst_ptt` double DEFAULT NULL,
  `prs_score` int DEFAULT NULL,
  `prs_ptt` double DEFAULT NULL,
  `ftr_score` int DEFAULT NULL,
  `ftr_ptt` double DEFAULT NULL,
  `byd_score` int DEFAULT NULL,
  `byd_ptt` double DEFAULT NULL,
  `etr_score` int DEFAULT NULL,
  `etr_ptt` double DEFAULT NULL,
  `time` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`scid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


-- 导出  表 aptc.t_user 结构
CREATE TABLE IF NOT EXISTS `t_user` (
  `uid` int NOT NULL AUTO_INCREMENT,
  `arc_id` varchar(9) DEFAULT NULL,
  `username` varchar(20) NOT NULL,
  `password` varchar(255) NOT NULL,
  `status` int DEFAULT '1',
  `ptt` double DEFAULT '0',
  `ptt_b30` double DEFAULT '0',
  `ptt_r10` double DEFAULT '0',
  PRIMARY KEY (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
