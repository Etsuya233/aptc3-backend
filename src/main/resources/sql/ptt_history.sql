use aptc;

create table t_ptt_history (
    uid int not null comment '用户id',
    ptt double not null comment '某日ptt',
    b30 double not null comment 'b30',
    r10 double not null comment 'r10',
    time date not null comment '日期',
    primary key (uid, time)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;