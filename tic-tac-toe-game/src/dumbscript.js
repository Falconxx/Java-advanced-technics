var make_move = function(board) {
    var len = board.length;
    print(len);
    var a = 0;
    var b = 0;
    while(board[a][b] != ' ') {
        a = Math.floor((Math.random() * len));
        b = Math.floor((Math.random() * len));
    }
    print('Dumb opponent making move');
    print('a=' + a);
    print('b=' + b);
    var l = [a,b];
    var ret = Java.to(l, "int[]");
    return ret;
}