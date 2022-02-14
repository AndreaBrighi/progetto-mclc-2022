push 0
/*ClassNode*/lhp
push function0
lhp
sw
lhp
push 1
add
shp
push function1
lhp
sw
lhp
push 1
add
shp
push 5
push 2
add
push function3
/*NewNode*/push 5
/*EmptyNode*/push -1
lhp
sw
lhp
push 1
add
shp
lhp
sw
lhp
push 1
add
shp
push 9998
lhp
sw
lhp
lhp
push 1
add
shp
lfp
lfp
stm
ltm
ltm
push -4
add
lw
js
push 1
beq label4
/*ClassCallNode*/lfp
lfp
stm
ltm
ltm
push 0
add
lw
js
b label5
label4:
lfp
push -3
add
lw
label5:
print
halt

/*MethodNode*/function0:
cfp
lra
lfp
lw
push -1
add
lw
stm
sra
pop
sfp
ltm
lra
js

/*MethodNode*/function1:
cfp
lra
lfp
lw
push -2
add
lw
stm
sra
pop
sfp
ltm
lra
js

function2:
cfp
lra
push -1
lfp
lfp
lw
lw
stm
ltm
ltm
push -4
add
lw
js
stm
pop
sra
pop
sfp
ltm
lra
js

function3:
cfp
lra
push function2
lfp
lw
push -3
add
lw
push 3
beq label2
push 0
b label3
label2:
push 1
label3:
push 1
beq label0
push 0
b label1
label0:
lfp
lfp
stm
ltm
ltm
push -2
add
lw
js
label1:
stm
pop
sra
pop
sfp
ltm
lra
js