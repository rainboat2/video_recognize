import sys
import paddlehub as hub
import json

videotag = hub.Module(name="videotag_tsn_lstm")
video_path = sys.argv[1]

results = videotag.classify(paths=[video_path], use_gpu=False)
prediction = results[0]['prediction']
print("rs:" + json.dumps(prediction))
