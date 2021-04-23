import sys

video_path = sys.argv[1]
cnt = 1
rs = 0
while cnt < 294794441:
    cnt += 1
    if cnt % 1234 == 0:
        rs += cnt
    else:
        rs -= cnt

print("hello world!" + str(rs) + video_path)
