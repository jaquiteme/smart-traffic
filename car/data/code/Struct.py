class Struct:
    def __init__(self, **config):
        self.__dict__.update(config)