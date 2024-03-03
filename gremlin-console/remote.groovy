:remote connect tinkerpop.server conf/remote.yaml session
:remote console

//Remove any existing data to allow this to be rerun
g.V().drop().iterate()

student = g.addV('student').property('name', 'Yang Fan').next()

topic = g.addV('knowledge').property('type', 'topic').property('name', 'Linear Relationships').next()
sub_topic = g.addV('knowledge').property('type', 'sub_topic').property('name', 'Basic Concepts of Cartesian Coordinate System').next()
content1 = g.addV('knowledge').property('type', 'content').property('name', 'Cartesian plane and ordered pairs').next()
content2 = g.addV('knowledge').property('type', 'content').property('name', 'Mid-point formula').next()
content3 = g.addV('knowledge').property('type', 'content').property('name', 'Distance formula').next()

sub_content1 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Order pairs').next()
sub_content2 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Cartesian plane').next()
sub_content3 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Applications of ordered pairs').next()
sub_content4 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Plotting coordinates on Cartesian planes').next()
sub_content5 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Mid-point').next()
sub_content6 = g.addV('knowledge').property('type', 'sub_content').property('name', 'Distance between two points').next()


g.addE('contains').from(topic).to(sub_topic).next()
g.addE('contains').from(sub_topic).to(content1).next()
g.addE('contains').from(sub_topic).to(content2).next()
g.addE('contains').from(sub_topic).to(content3).next()
g.addE('contains').from(content1).to(sub_content1).next()
g.addE('contains').from(content1).to(sub_content2).next()
g.addE('contains').from(content1).to(sub_content3).next()
g.addE('contains').from(content1).to(sub_content4).next()
g.addE('contains').from(content2).to(sub_content5).next()
g.addE('contains').from(content3).to(sub_content6).next()

g.addE('is pre-knowledge of').from(content1).to(content2).property('relevance', 0.3).next()
g.addE('is pre-knowledge of').from(content1).to(content3).property('relevance', 0.3).next()

g.addE('is pre-knowledge of').from(sub_content1).to(sub_content3).property('relevance', 0.8).next()
g.addE('is pre-knowledge of').from(sub_content1).to(sub_content4).property('relevance', 0.5).next()
g.addE('is pre-knowledge of').from(sub_content2).to(sub_content4).property('relevance', 0.7).next()


g.addE('focus').from(student).to(sub_content5).next()