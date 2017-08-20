
# Make jar file
#
# SRC_DIR -- a directory where put java files
# DEST_DIR -- a directory where put class files
# TARGET -- jar file
# MANIFEST -- manifest file

SRC_DIR = mylisp
DEST_DIR = build
TARGET = mylisp.jar
MANIFEST = $(SRC_DIR)/MANIFEST.MF

all: javac jar

$(DEST_DIR):
	mkdir -p $@

javac: $(DEST_DIR)
	javac -d $(DEST_DIR) $(SRC_DIR)/*.java

jar:
	jar cvfm $(TARGET) $(MANIFEST) -C $(DEST_DIR) .

clean:
	rm -r $(DEST_DIR)
