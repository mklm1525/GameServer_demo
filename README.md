# GameServer_demo
A Java game server, based on MINA 2.0

这是一个使用eclipse开发，基于mina2.0的游戏服务器程序。
请使用mina.main包下的Server类里的main方法启动
启动时需要传入两个main参数timezone=Asia/Shanghai和logpath=/gameServerLog分别代表当前时区和日志路径。
默认监听50000端口，可以在config/server.config.xml里修改
