# action-processor
Android异步处理库

容易使用，而且库代码简单易懂，代码量也不多。

### 代码演示

```
IProcessor processor = new ActionProcessor()
        .thread(ActionThread.BACKGROUND)
        .begin(new Action<Void, Boolean>() {
            @Override
            public Boolean doAction(Void data) {
                try {
                    Assets assets = new Assets(MainActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                } catch (IOException e) {
                    e.printStackTrace();
                    return false;
                }
                return true;
            }
        })
        .action(new Action<Boolean, List<Boolean>>() {
            @Override
            public List<Boolean> doAction(Boolean data) {
                List<Boolean> resultList = new ArrayList<>();
                resultList.add(data);
                resultList.add(!data);
                resultList.add(data);
                resultList.add(!data);
                resultList.add(data);
                resultList.add(data);
                resultList.add(data);
                resultList.add(data);
                resultList.add(data);
                return resultList;
            }
        })
        .take(1, 4)
        .map()
        .filter(new Action<Boolean, Boolean>() {
            @Override
            public Boolean doAction(Boolean data) {
                return data;
            }
        })
        .thread(ActionThread.MAIN)
        .end(new Action<Boolean, Void>() {
            @Override
            public Void doAction(Boolean data) {
                isReady = data;
                resultView.setText(isReady ? "初始化成功" : "初始化失败");
                return null;
            }
        });
processor.process();
```
