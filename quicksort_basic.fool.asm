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
push function2
lhp
sw
lhp
push 1
add
shp
push 5
push 2
add
/*NewNode*/push 55
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
lw
lhp
sw
lhp
lhp
push 1
add
shp
/*ClassCallNode*/lfp
lfp
push -4
add
lw
stm
ltm
ltm
lw
push 2
add
lw
js
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

/*MethodNode*/function2:
cfp
lra
lfp
lfp
lw
stm
ltm
ltm
/*diff*/lw
push 0
add
lw
js
lfp
lfp
lw
stm
ltm
ltm
/*diff*/lw
push 0
add
lw
js
mult
stm
sra
pop
sfp
ltm
lra
js