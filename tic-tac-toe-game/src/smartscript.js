var make_move = function(brd) {
    //var board = brd;
    //var arr = new java.util.Array();
    var board = brd;
    var len = board.length;
    var a = 0;
    var b = 0;
    for (i = 0; i < len; i++) {
        for (j = 0; j < len; j++) {
        print(board[i][j]);
            if(board[i][j] == 'O') {
                if(i+j < len && j+1 < len && board[i+1][j+1] == ' ') {
                    a = i + 1;
                    b = j + 1;
                    break;
                }
            }
        }
    }
    //var a = Math.floor((Math.random() * len));
    //var b = Math.floor((Math.random() * len));
    print('Smart opponent making move');
    print('a=' + a);
    print('b=' + b);
    var l = [a,b];
    var ret = Java.to(l, "int[]");
    return ret;
}