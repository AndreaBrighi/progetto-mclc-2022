push 0
push 5
push 2
add
push function1
lfp
lfp
stm
ltm
ltm
push -3
add
lw
js
push 1
beq label4
push 10
b label5
label4:
lfp
push -2
add
lw
label5:
print
halt

function0:
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
push -3
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

function1:
cfp
lra
push function0
lfp
lw
push -2
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