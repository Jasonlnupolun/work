/*
Navicat MySQL Data Transfer

Source Server         : db86.kanketv.com
Source Server Version : 50627
Source Host           : db86.kanketv.com:3306
Source Database       : jsyx

Target Server Type    : MYSQL
Target Server Version : 50627
File Encoding         : 65001

Date: 2016-12-21 19:22:54
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for t_userhistory
-- ----------------------------
DROP TABLE IF EXISTS `t_userhistory`;
CREATE TABLE `t_userhistory` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` varchar(50) DEFAULT NULL,
  `vodID` varchar(50) DEFAULT NULL,
  `typename` varchar(50) DEFAULT NULL,
  `kankeId` varchar(50) DEFAULT NULL,
  `title` varchar(100) DEFAULT NULL,
  `lpic` varchar(255) DEFAULT NULL,
  `addtime` datetime DEFAULT NULL,
  `year` varchar(255) DEFAULT NULL,
  `region` varchar(255) DEFAULT NULL,
  `tags` varchar(255) DEFAULT NULL,
  `score` float(4,2) DEFAULT NULL,
  `play_count` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2049 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of t_userhistory
-- ----------------------------
INSERT INTO `t_userhistory` VALUES ('2031', '427', '15', 'tv', 'tv_53454', '锦绣未央', 'http://tv.kanketv.com/image/tv/android/180x240/96/968FFF362A4F3A8137585.jpg', '2016-11-16 14:27:28', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2033', '427', '25', 'arts', 'arts_806670', '四大名助', 'http://tv.kanketv.com/image/arts/android/180x240/50/50B9F2FF089835B5806670.jpg', '2016-11-16 14:37:35', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2034', '427', '1166', 'tv', 'tv_53373', '人民检察官', 'http://tv.kanketv.com/image/tv/android/180x240/67/67EC3D752BBA4648855.jpg', '2016-11-16 14:44:18', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2035', '427', '5', 'film', 'film_28495', '爱', 'http://tv.kanketv.com/image/film/android/180x240/B8/B897DB54A4A9FD2044921.jpg', '2016-11-16 14:44:46', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2037', '427', '49', 'tv', 'tv_53242', '胭脂', 'http://tv.kanketv.com/image/tv/android/180x240/21/21ADB081B5D66B7253242.jpg', '2016-11-16 14:45:25', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2039', '427', '24', 'tv', 'tv_49589', '家国纪事', 'http://tv.kanketv.com/image/tv/android/180x240/78/782D55B4890F76D649589.jpg', '2016-11-16 14:45:50', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2040', '427', '27', 'tv', 'tv_51489', '远得要命的爱情', 'http://tv.kanketv.com/image/tv/android/180x240/79/797C153E80AAE09151489.jpg', '2016-11-16 14:45:59', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2041', '427', '18', 'arts', 'arts_38', '养生堂', 'http://tv.kanketv.com/image/arts/android/180x240/F0/F02065104375A85338.jpg', '2016-11-16 14:46:05', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2042', '427', '19', 'arts', 'arts_25651', 'x档案-安徽', 'http://ikanpic.kanketv.com/image/arts/android/180x240/B6/B6E245DF09314A1E25651.jpg', '2016-11-16 14:46:15', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2043', '427', '14', 'tv', 'tv_36970', '毛泽东', 'http://tv.kanketv.com/image/tv/android/180x240/60/60E4CFA074428BD036970.jpg', '2016-11-16 17:45:58', null, null, null, null, null);
INSERT INTO `t_userhistory` VALUES ('2044', '427', '14', 'tv', 'tv_36970', '毛泽东', 'http://tv.kanketv.com/image/tv/android/180x240/60/60E4CFA074428BD036970.jpg', '2016-11-16 17:45:58', '', '', '', null, null);
INSERT INTO `t_userhistory` VALUES ('2046', '427', '190', 'film', 'film_1433666', '上头条', 'http://tv.kanketv.com/image/film/android/180x240/44/449A82A54F5116F51433666.jpg', '2016-12-19 13:50:45', '2014', '内地;', '剧情;短片;喜剧;', '0.00', '12');
INSERT INTO `t_userhistory` VALUES ('2047', '442', '48267', 'film', 'film_1522643', '年兽大作战', 'http://tv.kanketv.com/image/film/android/180x240/5B/5BA5A992AD89D1C01522643.jpg', '2016-12-21 16:08:14', '2016', '内地;', '动画;奇幻;冒险;院线;预告片;国语;喜剧;', '0.00', '0');
INSERT INTO `t_userhistory` VALUES ('2048', '442', '3746', 'film', 'film_1523325', '拳拳到肉的艺术', 'http://tv.kanketv.com/image/film/android/180x240/1D/1DFEBB61351504C61523325.jpg', '2016-12-21 17:05:09', '2016', '内地;', '动作;', '0.00', '0');
