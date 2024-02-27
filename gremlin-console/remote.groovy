:remote connect tinkerpop.server conf/remote.yaml session
:remote console

//Remove any existing data to allow this to be rerun
g.V().drop().iterate()

topic = g.addV('knowledge').property('type', 'topic').property('name', 'Linear Relationships').next()
sub_topic = g.addV('knowledge').property('type', 'sub_topic').property('name', 'Basic Concepts of Cartesian Coordinate System').next()
content1 = g.addV('knowledge').property('type', 'content').property('name', 'Cartesian plane and ordered pairs').next()
content2 = g.addV('knowledge').property('type', 'content').property('name', 'Mid-point formula').next()
content3 = g.addV('knowledge').property('type', 'content').property('name', 'Distance formula').next()

outline1 = g.addV('outline').property('name', 'Order pairs').next()
outline2 = g.addV('outline').property('name', 'Cartesian plane').next()
outline3 = g.addV('outline').property('name', 'Applications of ordered pairs').next()
outline4 = g.addV('outline').property('name', 'Plotting coordinates on Cartesian planes').next()
outline5 = g.addV('outline').property('name', 'Mid-point').next()
outline6 = g.addV('outline').property('name', 'Distance between two points').next()


g.addE('contains').from(topic).to(sub_topic).next()
g.addE('contains').from(sub_topic).to(content1).next()
g.addE('contains').from(sub_topic).to(content2).next()
g.addE('contains').from(sub_topic).to(content3).next()

g.addE('contains').from(content1).to(outline1).next()
g.addE('contains').from(content1).to(outline2).next()
g.addE('contains').from(content1).to(outline3).next()
g.addE('contains').from(content1).to(outline4).next()

g.addE('contains').from(content2).to(outline5).next()

g.addE('contains').from(content3).to(outline6).next()