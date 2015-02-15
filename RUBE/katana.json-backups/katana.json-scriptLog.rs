//Started script log at Sat Feb 14 00:23:44 2015

getImage(1).select();
getImage(1).setFile('/Users/yin/Pictures/Katana.png');
getBody(1).select();
getBody(1).deselect();
addBody(2, '{"awake":true,"type":"dynamic"}');
getBody(2).addFixture(11, '{"density":1,"shapes":[{"radius":0,"type":"polygon"}],"friction":0.2,"vertices":{"x":[-0.5,0.5,0.5,-0.5],"y":[-0.5,-0.5,0.5,0.5]}}');
getBody(2).setPosition(0,0);
getFixture(11).select();
getVertex(11,1).select();
getVertex(11,2).select();
getVertex(11,1).setPos(3.31304, -0.5);
getVertex(11,2).setPos(3.31304, 0.5);
getVertex(11,1).deselect();
getVertex(11,2).deselect();
getVertex(11,0).select();
getVertex(11,3).select();
getVertex(11,0).setPos(-3.15995, -0.5);
getVertex(11,3).setPos(-3.15995, 0.5);
getBody(2).select();
getBody(2).setType(0);
getBody(2).deselect();
setCursor(-2.12743, 1.35396);
setCursor(-2.1083, 1.38267);
addBody(3, '{"awake":true,"type":"dynamic"}');
getBody(3).addFixture(12, '{"density":1,"shapes":[{"radius":0,"type":"polygon"}],"friction":0.2,"vertices":{"x":[-0.5,0.5,0.5,-0.5],"y":[-0.5,-0.5,0.5,0.5]}}');
getBody(3).setPosition(-2.1083,1.38267);
